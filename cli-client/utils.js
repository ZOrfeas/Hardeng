const fs = require('fs');

function isValidApikey (apikey) {
    const regex = RegExp('([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})');
    return regex.test(apikey);
}

function isValidDate(date) {
    const regex = RegExp('[0-9]{8}');
    return regex.test(date);
}

function createTokenFile(token) {
    try {
        if (!tokenFileExists()) {
            fs.writeFileSync(getTokenFilePath(), token);
            console.log('Successfully logged in');
        }
        else {
            console.log('Already logged in. Please log out first');
        }
    } catch(err) {
        console.error('An unexpected error occured');
    }
}

function deleteTokenFile () {
    if (tokenFileExists()) {
        try {
            fs.unlinkSync(getTokenFilePath());
            console.log('Successfully logged out');
        } catch(err) {
            console.error('An unexpected error occured');
        }
    }
    else {
        console.log('Successfully logged out');
    }
}

function tokenFileExists () {
    return fs.existsSync(getTokenFilePath());
}

function getToken () {
    return fs.readFileSync(getTokenFilePath()).toString();
}

function getTokenFilePath () {
    const homeDir = require('os').homedir();
    const filePath = homeDir + '/softeng20bAPI.token';
    return filePath;
}

module.exports = { isValidApikey,
                   isValidDate,
                   createTokenFile,
                   deleteTokenFile,
                   tokenFileExists,
                   getToken };