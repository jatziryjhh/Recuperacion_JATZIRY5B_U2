import { createContext } from "react";

// nos ayuda a saber que usuario está en sesión y cual no
// User signed - null
// user - dispatch ------------ esto nos dirá si tiene algún cambio
const AuthContext = createContext();

export default AuthContext;

