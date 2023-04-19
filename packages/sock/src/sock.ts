import { Connection } from './connection';
import { v4 as uuidv4 } from 'uuid';
import { ConnectionOptions } from './options';

export class Sock {
  baseUrl: string | undefined;

  connections: Map<string, Connection> = new Map<string, Connection>();

  static interceptors: Array<() => void> = new Array<() => void>();

  private constructor() {}

  public static build(baseUrl: string): Sock {
    const sock = new Sock();
    sock.baseUrl = baseUrl;
    return sock;
  }

  public connect(options: ConnectionOptions): Connection {
    // return new Sock();
    uuidv4();
    console.log(uuidv4());
    const conn = new Connection(options);
    this.connections.set(uuidv4(), conn);
    return conn;
  }
}
