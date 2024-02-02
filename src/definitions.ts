export interface ufacesdkPlugin {
  verify(options: { baseurl: string }): Promise<ScoreResult>;
  test(options: {baseurl:string}) : Promise<ScoreResult> ; 
}

export interface ScoreResult { 
   value : string ; 
}

export interface ConfigOptions { 
  baseurl: string ; 
  token: string ;
}