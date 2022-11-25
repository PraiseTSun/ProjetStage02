import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@react-pdf-viewer/core/lib/styles/index.css';
import {Worker} from '@react-pdf-viewer/core';

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);
root.render(
    <React.StrictMode>
        <div className="root">
            <Worker workerUrl="https://unpkg.com/pdfjs-dist@2.16.105/build/pdf.worker.min.js">
                <App/>
            </Worker>
        </div>
    </React.StrictMode>
);
