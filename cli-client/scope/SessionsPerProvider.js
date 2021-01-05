const axios = require('axios');
const { isValidDate, tokenFileExists, getToken } = require("../utils");

exports.command = 'SessionsPerProvider'

exports.desc = "Charging sessions in a time period by an electricity provider"

exports.builder ={
    provider: {
        describe: "Electricity provider",
        demand: true
    },
    datefrom: {
        describe:"Starting date",
        demand: true
    },
    dateto:{
        describe: "Ending date",
        demand:true
    }
}

exports.handler = function(argv)
{
    if(tokenFileExists())
    {
        const token = getToken();
        const provid = argv.provider;
        if(!isValidDate(argv.datefrom))
        {
            console.log('Invalid datefrom format: Use YYYYMMDD format instead');
            return;
        }
        if(!isValidDate(argv.dateto))
        {
            console.log('Invalid dateto format: Use YYYYMMDD format instead');
            return;
        } 
        const start = argv.datefrom;
        const finish = argv.dateto;
        const endpoint =  '/SessionsPerProvider'+ '/' + provid + '/' + start + '/' + finish;
        axios.get(endpoint, {
            params:{
                format: argv.format,
                apikey: argv.apikey
            },
            headers: {
                'X-OBSERVATORY-AUTH': token
            }
        })
        .then(res =>{
            console.log(res.data);
        })
        .catch(err => {
            console.log(err.response.data)
        })

    }
    else
    {
        console.log('You are not logged in. Please log in first')
    }
}