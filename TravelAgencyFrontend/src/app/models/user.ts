import { Role } from "./role";

export interface User{
    id:number | undefined;
    first_name:string;
    last_name:string ;
    username:string;
    email:string;
    phone:string;
    password:string;
    roles: Role [] ;
}
