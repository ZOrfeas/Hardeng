const fs = require('fs');
const utils = require('../utils');

describe('Utils', function() {

    describe('isValidDate', function () {

        test('should return false when invalid symbols used', function() {
            expect(utils.isValidDate('20a00102')).toBe(false);
        });

        test('should return false when less numbers used', function() {
            expect(utils.isValidDate('2020012')).toBe(false);
        });

        test('should return false when more numbers used', function() {
            expect(utils.isValidDate('202001023')).toBe(false);
        });

        test('should return true', function() {
            expect(utils.isValidDate('20200102')).toBe(true);
        });
    });

    describe('createTokenFile', function() {

        beforeAll(() => {
            if (utils.tokenFileExists())
                fs.unlinkSync(utils.getTokenFilePath());
        });

        beforeEach(() => {
            console.log = jest.fn();
        });

        it("should log user succesfully", function() {
            utils.createTokenFile('token');
            expect(console.log).toHaveBeenCalledTimes(1);
            expect(console.log).toHaveBeenCalledWith('Successfully logged in');
        });

        it("should fail to log user", function() {
            utils.createTokenFile('token');
            expect(console.log).toHaveBeenCalledTimes(1);
            expect(console.log).toHaveBeenCalledWith('Already logged in. Please log out first');
        });

    });

    describe('deleteTokenFile', function() {

        beforeAll(() => {
            if (utils.tokenFileExists())
                fs.unlinkSync(utils.getTokenFilePath());
        });

        it("should log out user succesfully", function() {
            utils.createTokenFile('token');

            console.log = jest.fn();
            utils.deleteTokenFile('token');
            expect(console.log).toHaveBeenCalledTimes(1);
            expect(console.log).toHaveBeenCalledWith('Successfully logged out');
        });

        it("should log out user succesfully", function() {
            console.log = jest.fn();
            utils.deleteTokenFile('token');
            expect(console.log).toHaveBeenCalledTimes(1);
            expect(console.log).toHaveBeenCalledWith('Successfully logged out. Token file missing');
        });

    });

    describe('errorHandler', function() {

        it("should print Error: message", function() {
            console.log = jest.fn();
            utils.errorHandler({ message: "message" })
            expect(console.log).toHaveBeenCalledTimes(1);
            expect(console.log).toHaveBeenCalledWith('Error: ','message');
        });

        it("should print response.data", function() {
            console.log = jest.fn();
            utils.errorHandler({ response: { data: "data" } })
            expect(console.log).toHaveBeenCalledTimes(1);
            expect(console.log).toHaveBeenCalledWith('data');
        });

    });

});


