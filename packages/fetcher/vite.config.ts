import { defineConfig } from 'vite';
import dtsPlugin from 'vite-plugin-dts';

export default defineConfig({
  build: {
    lib: {
      entry: './src/index.ts',
      name: 'fetch',
      fileName: 'fetch',
    },
  },
  plugins: [dtsPlugin()],
});
