const axios = require("axios");
const { errorHandler, getToken, tokenFileExists } = require("../utils");

exports.command = 'resetsessions'

exports.desc = 'Initialize charging events & default admin account'

exports.handler = function(argv) {
    if(tokenFileExists())
    {
        const token = getToken();  
        axios.post('/admin/resetsessions', {
            headers: {
                'X-OBSERVATORY-AUTH': token.toString()
            }
        })
        .then(res => {
            console.log(res.data);
        })
        .catch(err => {
            errorHandler(err);
        })
    }
    else{
        console.log('You are not logged in. Please log in first')
    }
}