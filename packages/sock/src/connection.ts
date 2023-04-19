import { ConnectionOptions } from './options';

export class Connection {
  connection: WebSocket;

  options: ConnectionOptions;

  constructor(options: ConnectionOptions) {
    this.connection = new WebSocket(options.url, options.protocols);
    this.options = options;
  }

  close(): boolean {
    return this.connection.CLOSED == 1;
  }

  send(data: string): void;
  send(data: Blob): void;
  send(data: ArrayBuffer): void;
  send(data: ArrayBufferView): void;
  send(data: any): void {
    switch (typeof data) {
      case 'string':
        break;
    }
  }
}
