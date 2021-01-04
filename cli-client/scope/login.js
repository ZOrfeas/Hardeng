const axios = require("axios");
const { tokenFileExists, createTokenFile } = require("../utils");

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
    }
}

exports.handler = function(argv) {

    if (!tokenFileExists()) {

        axios.post('/login', {
            params: {
                format: argv.format,
                apikey: argv.apikey,
                username: argv.username,
                password: argv.passw
            },
            headers: {
                'content-type': 'application/x-www-form-urlencoded'
            }
        })
        .then(res => {
            const token = (argv.format === 'csv') ? res.data.substring(6) : res.data.token;
            createTokenFile(token);
        })
        .catch(err => {
            console.log(err.response.data);
        })
    }
    else {
        console.log('Already logged in. Please log out first');
    } 
}