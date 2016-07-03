# Workshop Registration

## Development

### Generate Eclipse files

    ./activator eclipse

### Run tests

    ./activator test

### Run application

    ./activator run

### Build zip

    ./activator dist

### Generate application secret and start application

    ./activator playGenerateSecret 
    export APPLICATION_SECRET=JWD...     
    ./bin/workshop-registration -Dplay.crypto.secret="JWD..."

## Release and Deployment

### Update version number in `build.sbt`

### Build deb

    ./activator debian:packageBin

### Upload to Github

### Copy to server

### Install on server

    cd /tmp
    wget https://github.com/cko/workshop-registration/releases/download/v1.2/workshop-registration_1.2_all.deb
    dpkg -i workshop-registration_1.2_all.deb


## Database commands

### Get registrations

    sqlite3 db.db "select * from registrations"

### Update password

    UPDATE passwordinfo set password = '$2a$...' where loginInfoId = 1;

### Add workshop

    INSERT INTO workshops VALUES (1, '2016-07-01T00:00:00Z', '2016-08-01T00:00:00Z', 15, 'when',  'title', 'description');
