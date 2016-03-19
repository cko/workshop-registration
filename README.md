

## Deployment

### Build zip

    ./activator dist 

### Generate application secret and start application

    ./activator playGenerateSecret 
    export APPLICATION_SECRET=JWD...     
    ./bin/workshop-registration -Dplay.crypto.secret="JWD..."

