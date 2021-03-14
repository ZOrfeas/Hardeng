const axios = require("axios");
const { errorHandler, getToken, tokenFileExists } = require("../utils");

exports.command = 'healthcheck'

exports.desc = 'Check user-database connection'

exports.handler = function(argv) {
    if(tokenFileExists())
    {
        const token = getToken();
        axios.get('/admin/healthcheck', {
            headers:{
                'X-OBSERVATORY-AUTH': token.toString()
            }
        })
        .then(res => {
            console.log(res.data);
        })
        .catch(err => {
            console.log("here");
            errorHandler(err);
        })
    }
    else{
        console.log('You are not logged in. Please log in first')
    }
}