const axios = require("axios");
const { tokenFileExists, deleteTokenFile, getToken, errorHandler } = require("../utils");

exports.command = 'logout'

exports.desc = 'Logout user'

exports.handler = function(argv) {

    if (tokenFileExists()) {
        /*const token = getToken();

        axios.post('/logout', null, {
            headers: {
                'X-OBSERVATORY-AUTH': token.toString()
            }
        })
        .then(() => {
            deleteTokenFile();
        })
        .catch(err => {
            errorHandler(err);
        })*/
        deleteTokenFile();
    }
    else {
        console.log('You are not logged in. Please log in first');
    } 
}