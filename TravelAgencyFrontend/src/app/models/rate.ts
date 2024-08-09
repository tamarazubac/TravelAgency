import { Arrangement } from "./arrangement";
import { User } from "./user";

export interface Rate{
  id:number|undefined;
  comment:string;
  user:User;
  arrangement:Arrangement;
  rateNum:number;
}
