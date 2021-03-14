const fs = require('fs');
const { createTokenFile, tokenFileExists, getTokenFilePath } = require("../utils");
const axios = require('axios');
const admin = require('../scope/Admin');
const healthcheck = require('../scope/healthcheck');
const login = require('../scope/login');
const logout = require('../scope/logout');
const resetsessions = require('../scope/resetsessions');
const sessionsPerEV = require('../scope/SessionsPerEV');
const sessionsPerPoint = require('../scope/SessionsPerPoint');
const sessionsPerProvider = require('../scope/SessionsPerProvider');
const sessionsPerStation= require('../scope/SessionsPerStation');

jest.mock('axios');

describe('Healthcheck', function() {
    beforeAll(() => {
        jest.mock('axios');
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        createTokenFile('token');
    });

    it("should return {status:'OK'} if database is connected properly", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        console.log = jest.fn();
        await expect(healthcheck.handler());
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith({ status: 'OK' });
    });

    /*it("should report an error if database is not connected properly", async function() {
        axios.get.mockImplementationOnce(() => Promise.reject(new Error({ response: { data: "error" } })));
        console.log = jest.fn();
        await expect(healthcheck.handler());
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('error');
    });*/
});


describe('Reset Sessions', function() {
    beforeAll(() => {
        jest.mock('axios');
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        createTokenFile('token');
    });

    it("should return {status:'OK'} if succeeds", async function() {
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        console.log = jest.fn();
        await expect(resetsessions.handler());
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith({ status: 'OK' });
    });

    /*it("should report an error if it does not succeed", async function() {
        axios.get.mockImplementationOnce(() => Promise.reject(new Error({ response: { data: "error" } })));
        console.log = jest.fn();
        await expect(resetsessions.handler());
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('error');
    });*/
});


describe('Login', function() {
    beforeAll(() => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
    });

    it("should return 'Successfully logged in'", async function() {
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { token: 'token' } }));
        console.log = jest.fn();
        await expect(login.handler({ username: 'driver2', password: 'driver2', role: 'DRIVER'}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Successfully logged in');
    });

    it("should create ${HOME}/softeng20bAPI.token", function() {
      expect(fs.existsSync(require('os').homedir() + '/softeng20bAPI.token')).toBe(true);
    });

    it("should return 'Already logged in. Please log out first'", function() {
        console.log = jest.fn();
        login.handler({ username: 'driver2', password: 'driver2', role: 'DRIVER'});
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Already logged in. Please log out first');
    });
});


describe('Logout', function() {
    beforeAll(async () => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        await login.handler({ username: 'user2', password: 'passw2', role: 'STATION_ADMIN'});
    });

    it("should return 'Successfully logged out'", function() {
        console.log = jest.fn();
        logout.handler();
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Successfully logged out');
    });

    it("should remove ${HOME}/softeng20bAPI.token", function() {
        expect(fs.existsSync(require('os').homedir() + '/softeng20bAPI.token')).toBe(false);
    });

    it("should return 'You are not logged in. Please log in first'", function() {
        console.log = jest.fn();
        logout.handler();
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('You are not logged in. Please log in first');
    });
});

describe('SessionsPerEV', function() {
    beforeAll(async () => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        await login.handler({ username: 'user2', password: 'passw2', role: 'STATION_ADMIN'});
    });

    it("should return result if succeeds", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerEV.handler({ dateto: 20181231, datefrom: 20181101, ev: '1-13'}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('result');
    });

    it("should return invalid dateto", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerEV.handler({ dateto: 2018123, datefrom: 20181101, ev: '1-13'}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid dateto format: Use YYYYMMDD format instead');
    });

    it("should return invalid datefrom", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerEV.handler({ dateto: 20181231, datefrom: 201811, ev: '1-13'}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid datefrom format: Use YYYYMMDD format instead');
    });

    it("should return user not logged in", async function() {
        fs.unlinkSync(getTokenFilePath());
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerEV.handler({ dateto: 20181231, datefrom: 20181101, ev: '1-13'}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('You are not logged in. Please log in first');
    });
});

describe('SessionsPerPoint', function() {
    beforeAll(async () => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        await login.handler({ username: 'user2', password: 'passw2', role: 'STATION_ADMIN'});
    });

    it("should return result if succeeds", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerPoint.handler({ dateto: 20181231, datefrom: 20181101, point: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('result');
    });

    it("should return invalid dateto", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerPoint.handler({ dateto: 2018123, datefrom: 20181101, point: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid dateto format: Use YYYYMMDD format instead');
    });

    it("should return invalid datefrom", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerPoint.handler({ dateto: 20181231, datefrom: 201811, point: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid datefrom format: Use YYYYMMDD format instead');
    });

    it("should return user not logged in", async function() {
        fs.unlinkSync(getTokenFilePath());
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerPoint.handler({ dateto: 20181231, datefrom: 20181101, point: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('You are not logged in. Please log in first');
    });
});

describe('SessionsPerProvider', function() {
    beforeAll(async () => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        await login.handler({ username: 'user2', password: 'passw2', role: 'STATION_ADMIN'});
    });

    it("should return result if succeeds", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerProvider.handler({ dateto: 20181231, datefrom: 20181101, provider: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('result');
    });

    it("should return invalid dateto", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerProvider.handler({ dateto: 2018123, datefrom: 20181101, provider: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid dateto format: Use YYYYMMDD format instead');
    });

    it("should return invalid datefrom", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerProvider.handler({ dateto: 20181231, datefrom: 201811, provider: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid datefrom format: Use YYYYMMDD format instead');
    });

    it("should return user not logged in", async function() {
        fs.unlinkSync(getTokenFilePath());
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerProvider.handler({ dateto: 20181231, datefrom: 20181101, provider: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('You are not logged in. Please log in first');
    });
});

describe('SessionsPerStation', function() {
    beforeAll(async () => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        await login.handler({ username: 'user2', password: 'passw2', role: 'STATION_ADMIN'});
    });

    it("should return result if succeeds", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerStation.handler({ dateto: 20181231, datefrom: 20181101, station: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('result');
    });

    it("should return invalid dateto", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerStation.handler({ dateto: 2018123, datefrom: 20181101, station: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid dateto format: Use YYYYMMDD format instead');
    });

    it("should return invalid datefrom", async function() {
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerStation.handler({ dateto: 20181231, datefrom: 201811, station: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('Invalid datefrom format: Use YYYYMMDD format instead');
    });

    it("should return user not logged in", async function() {
        fs.unlinkSync(getTokenFilePath());
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(sessionsPerStation.handler({ dateto: 20181231, datefrom: 20181101, station: 1}));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('You are not logged in. Please log in first');
    });
});

describe('Admin', function() {
    beforeAll(async () => {
        if (tokenFileExists())
            fs.unlinkSync(getTokenFilePath());
        axios.post.mockImplementationOnce(() => Promise.resolve({ data: { status: 'OK' } }));
        await login.handler({ username: 'user2', password: 'passw2', role: 'STATION_ADMIN'});
    });

    it("should return user not logged in", async function() {
        fs.unlinkSync(getTokenFilePath());
        axios.get.mockImplementationOnce(() => Promise.resolve({ data: 'result' }));
        console.log = jest.fn();
        await expect(admin.handler({ users: 1 }));
        expect(console.log).toHaveBeenCalledTimes(1);
        expect(console.log).toHaveBeenCalledWith('You are not logged in. Please log in first');
    });
});