import {ConnectionOptions} from './options';



export class WsConnection{
    connection: WebSocket;

    options: ConnectionOptions

    constructor(options: ConnectionOptions) {
        if (options.url === undefined)
            throw new Error('websocket connection url is not specify');
        this.connection = new WebSocket(options.url, options.protocols);
        this.options = options;
    }

    close(): boolean {
        return false;
    }

    send(data: string): void;
    send(data: Blob): void;

    send(data: any): void {
        switch (typeof data){
            case "string":

                break;
        }
    }
}
