import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@react-pdf-viewer/core/lib/styles/index.css';
import background from './img/Background.png'
import { Worker } from '@react-pdf-viewer/core';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <div style={{ backgroundColor: "black", backgroundImage: `url(${background})`, backgroundSize: "cover" }}>
      <Worker workerUrl="https://unpkg.com/pdfjs-dist@2.16.105/build/pdf.worker.min.js">
        <App />
      </Worker>
    </div>
  </React.StrictMode>
);
