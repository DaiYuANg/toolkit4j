import { describe } from 'vitest';
import { Sock } from '../src';

describe('test sock', function () {
  const sock = Sock.build('localhost');
  sock.connect('http://localhost:8080', undefined);
});
