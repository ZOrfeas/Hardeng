const { execSync } = require("child_process");
const assert = require('assert');
const fs = require('fs');

const test = (args) => {
    return execSync(`node ./index.js ${args}`).toString();
};

describe('Healthcheck', function() {
  describe('/admin/healthcheck', function() {

    it("should return {status:'OK'} if database is connected properly", function() {
      assert.equal(test("healthcheck"), "{ status: 'OK' }\n");
    });
  });
});

/*
describe('Reset Sessions', function() {
  describe('/admin/resetsessions', function() {

    it("should return {status:'OK'} if succeeds", function() {
      assert.equal(test("resetsessions"), "{ status: 'OK' }\n");
    });
  });
});
*/

describe('Login', function() {
  describe('/login', function() {

    it("should return 'Successfully logged in'", function() {
      let cmd = "login --username driver2 --passw driver2 --role driver";
      assert.equal(test(cmd), "Successfully logged in\n");
    });

    it("should create ${HOME}/softeng20bAPI.token", function() {
      assert.equal(fs.existsSync(require('os').homedir() + '/softeng20bAPI.token'), true);
    });

  });
});

describe('Logout', function() {
  describe('/logout', function() {

    it("should return 'Successfully logged out'", function() {
      let cmd = "logout";
      assert.equal(test(cmd), "Successfully logged out\n");
    });

    it("should remove ${HOME}/softeng20bAPI.token", function() {
      assert.equal(fs.existsSync(require('os').homedir() + '/softeng20bAPI.token'), false);
    });

  });
});