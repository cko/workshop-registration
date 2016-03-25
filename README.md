# Workshop Registration

## Development

### Generate Eclipse files

    ./activator eclipse

### Run tests

    ./activator test

### Run application

    ./activator run

## Deployment

### Build zip

    ./activator dist 

### Generate application secret and start application

    ./activator playGenerateSecret 
    export APPLICATION_SECRET=JWD...     
    ./bin/workshop-registration -Dplay.crypto.secret="JWD..."

### Build deb

    ./activator debian:packageBin

## Database commands

### Get registrations

    sqlite3 db.db "select * from registrations"

### Update password

    UPDATE passwordinfo set password = '$2a$...' where loginInfoId = 1;


