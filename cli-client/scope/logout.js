const axios = require("axios");
const { tokenFileExists, deleteTokenFile, getToken } = require("../utils");

exports.command = 'logout'

exports.desc = 'Logout user'

exports.handler = function(argv) {

    if (tokenFileExists()) {
        const token = getToken();

        axios.post('/logout', {
            query: {
                format: argv.format,
                apikey: argv.apikey
            },
            headers: {
                'X-OBSERVATORY-AUTH': token
            }
        })
        .then(() => {
            deleteTokenFile();
        })
        .catch(err => {
            console.log(err.response.data);
        })
    }
    else {
        console.log('You are not logged in. Please log in first');
    } 
}