const axios = require("axios");
const { isValidDate, tokenFileExists, getToken, errorHandler } = require("../utils");

exports.command = 'SessionsPerPoint'

exports.desc = 'Get charging sessions of a charging point for a specified time period'

exports.builder = {
    point: {
      describe: 'Pass the charging point ID',
      demand: true
    },
    datefrom: {
      describe: 'Pass the starting date',
      demand: true
    },
    dateto: {
        describe: 'Pass the ending date',
        demand: true
    },
    format: {
      demand: true,
      describe: 'Choose output format',
      choices: ['csv', 'json']
    }
}

exports.handler = function(argv) {

    if (tokenFileExists()) {
        const token = getToken();
        
        const pointID = argv.point;

        if (!isValidDate(argv.datefrom))
        {
            console.log('Invalid datefrom format: Use YYYYMMDD format instead');
            return;
        }

        if (!isValidDate(argv.dateto))
        {
            console.log('Invalid dateto format: Use YYYYMMDD format instead');
            return;
        }

        const periodFrom = argv.datefrom;
        const periodTo = argv.dateto;

        const endpoint = '/SessionsPerPoint' + '/' + pointID + '/' + periodFrom + '/' + periodTo;
        axios.get(endpoint, {
            params: {
                format: argv.format
            },
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
    else {
        console.log('You are not logged in. Please log in first');
    } 
}