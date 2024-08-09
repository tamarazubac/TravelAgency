import { User } from 'src/app/models/user';
import { Arrangement } from "./arrangement";

export interface Reservation{

  id:number |undefined;
  number_of_people:number;
  full_price:number;
  arrangement:Arrangement;
  user:User;


}
