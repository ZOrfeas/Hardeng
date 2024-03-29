const axios = require("axios");
const qs = require('querystring');
const { tokenFileExists, createTokenFile, errorHandler } = require("../utils");

exports.command = 'login'

exports.desc = 'Login user'

exports.builder = {
    username: {
      describe: 'Pass the username',
      demand: true
    },
    passw: {
      describe: 'Pass the password',
      demand: true
    },
    role: {
      demandOption: true,
      describe: 'Choose user role',
      choices: ['STATION_ADMIN', 'DRIVER']
    }
}

exports.handler = function(argv) {

    if (!tokenFileExists()) {

        axios.post('/login/' + argv.role, qs.stringify({
            username: argv.username,
            password: argv.passw
            }), {
            headers: {
                'content-type': 'application/x-www-form-urlencoded'
            }
        })
        .then(res => {
            const token = res.data.token;
            createTokenFile(token);
        })
        .catch(err => {
            errorHandler(err);
        })
    }
    else {
        console.log('Already logged in. Please log out first');
    } 
}