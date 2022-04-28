module.exports = {
    
    'src_folders': ['tests/e2e/specs'
    ],
    'output_folder': 'tests/e2e/reports',
    'globals_path': 'tests/e2e/global.js',
    'selenium': {
        'start_process': true,
        'server_path': require('selenium-server').path,
        'port': 9515,
        'cli_args': {
            'webdriver.chrome.driver': require('chromedriver').path
        }
    },

    'test_settings': {
        'default': {
            'selenium_port': 9515,
            'selenium_host': 'localhost',
            'silent': true,
            'globals': {
                'productListUrl': 'http: //localhost:' + 9003 + '/productlist.html',
            }
        },

        'chrome': {
            'desiredCapabilities': {
                'browserName': 'chrome',
                'javascriptEnabled': true,
                'acceptSslCerts': true,
                'chromeOptions': {
                    'args': [
                       '--headless',
                     '--disable-gpu'
                    ],
                    'binary': '/opt/google/chrome/google-chrome'
                }
            }
        },

        'globals': {
            'productListUrl': 'http: //localhost:' + 9003 + '/productlist.html',
        }
    }
}