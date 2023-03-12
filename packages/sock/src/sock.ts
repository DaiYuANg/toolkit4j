import { WsConnection } from './ws.connection';
import { v4 as uuidv4 } from 'uuid';
import { ConnectionOptions } from './options';

export class Sock {
  baseUrl: string | undefined;

  connections: Map<string, WsConnection> = new Map<string, WsConnection>();

  static interceptors: Array<() => void> = new Array<() => void>();

  private constructor() {}

  public static build(baseUrl: string): Sock {
    const sock = new Sock();
    sock.baseUrl = baseUrl;
    return sock;
  }

  public connect(options: ConnectionOptions): WsConnection {
    // return new Sock();
    uuidv4();
    console.log(uuidv4());
    const conn = new WsConnection(options);
    this.connections.set(uuidv4(), conn);
    return conn;
  }
}
