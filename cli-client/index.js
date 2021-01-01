#!/usr/bin/env node

require('dotenv').config()

const axios = require("axios");

axios.defaults.baseURL = process.env.BASE_URL;

require('yargs/yargs')(process.argv.slice(2))
  .commandDir('scope')
  .demandCommand(1)
  .strict()
  .option('format', {
      demandOption: true,
      describe: 'Choose output format',
      choices: ['csv', 'json']
  })
  .option('apikey', {
      demandOption: true,
      describe: 'Specify API key'
  })
  .check((argv) => {
    const regex = RegExp('([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})');
    if (!regex.test(argv.apikey)) {
        throw new Error('Invalid API key format')
    }
    return true;
  })
  .help()
  .argv