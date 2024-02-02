import { SplashScreen } from '@capacitor/splash-screen';
import { Camera } from '@capacitor/camera';
import { ufacesdk , ConfigOptions , ScoreResult } from 'ufacesdk/src/' ;

window.customElements.define(
  'capacitor-welcome',
  class extends HTMLElement {
    constructor() {
      super();

      SplashScreen.hide();

      const root = this.attachShadow({ mode: 'open' });

      root.innerHTML = `
    <style>
      :host {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
        display: block;
        width: 100%;
        height: 100%;
      }
      h1, h2, h3, h4, h5 {
        text-transform: uppercase;
      }
      .button {
        display: inline-block;
        padding: 10px;
        background-color: #73B5F6;
        color: #fff;
        font-size: 0.9em;
        border: 0;
        border-radius: 3px;
        text-decoration: none;
        cursor: pointer;
      }
      main {
        padding: 15px;
      }
      main hr { height: 1px; background-color: #eee; border: 0; }
      main h1 {
        font-size: 1.4em;
        text-transform: uppercase;
        letter-spacing: 1px;
      }
      main h2 {
        font-size: 1.1em;
      }
      main h3 {
        font-size: 0.9em;
      }
      main p {
        color: #333;
      }
      main pre {
        white-space: pre-line;
      }
    </style>
    <div>
      <capacitor-welcome-titlebar>
        <h1>UFace SDK</h1>
      </capacitor-welcome-titlebar>
      <main>
        <p>
          UFace SDK enables you to verify the face with photo.
        </p>
        <p>
        <button class="button" id="verifybtn">Verify</button>
      </p>
      <p>
      <button class="button" id="testbtn">Test</button>
    </p>
      </main>
    </div>
    `;
    }

    connectedCallback() {
      const self = this;


      ufacesdk.addListener("onVerified" , (info) => {
        console.log(info);
        alert("score::" + info.value) ; 
      } );

      self.shadowRoot.querySelector('#verifybtn').addEventListener('click', async function (e) {

          console.log("###### Start Verify #####") ;
          const config  =   { baseurl: "http://ekyc.metsafr.com:3333/api/invoke"  , 
          token : "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzZXJ2aWNlIjoib25lNHUiLCJzdWIiOiJvbmU0dSIsImp0aSI6IjE0YTdiZTExLTY1N2UtNDgwZi05M2I4LTAzMGM0ODA1ZWY4MCIsImlhdCI6MTcwMjUzMjAzMiwiZXhwIjoxNzM0MDY4MDMyfQ.4eFOmxM-WQQeucB7HWvTE2ksKindvJ2xEfgngFiQ0Ok" };
          ufacesdk.verify(config).then((result) => {
            console.log(config); 
          }) ; 

      });

    }
  }
);

window.customElements.define(
  'capacitor-welcome-titlebar',
  class extends HTMLElement {
    constructor() {
      super();
      const root = this.attachShadow({ mode: 'open' });
      root.innerHTML = `
    <style>
      :host {
        position: relative;
        display: block;
        padding: 15px 15px 15px 15px;
        text-align: center;
        background-color: #73B5F6;
      }
      ::slotted(h1) {
        margin: 0;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
        font-size: 0.9em;
        font-weight: 600;
        color: #fff;
      }
    </style>
    <slot></slot>
    `;
    }
  }
);
