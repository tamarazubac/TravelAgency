import { Destination } from "./destination";
import { User } from "./user";

export interface Arrangement {
  id:number|undefined;
  date_from:Date;
  date_to:Date;
  description:string;
  free_seats:number;
  price_per_person:number;
  destination:Destination |undefined;
  owner:User |undefined;
}
