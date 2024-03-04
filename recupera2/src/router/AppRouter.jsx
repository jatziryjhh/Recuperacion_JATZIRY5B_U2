import React, { useContext } from "react";
import SignInPage from "../modules/auth/SignInPage";
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import AuthContext from "../config/context/auth-context";
import AdminLayout from "../modules/admin/AdminLayout";

const AppRouter = () => {
  const userRole = localStorage.getItem("userRole");
  const username = localStorage.getItem("username");
  const { user } = useContext(AuthContext);

  const router = createBrowserRouter(
    createRoutesFromElements(
      <>
        {user.signed ? (
          <>
            <Route
              path="/"
              element={
                userRole === "admin" ? (
                  <AdminLayout username={username} userRole={userRole} />
                ) : (
                  <>404 NOT FOUND</>
                )
              }
            />
            <>
            <Route
              path="/admin"
              element={
                userRole === "admin" ? (
                  <>
                    ¡USTED ES {username}, BIENVENIDO! SU ROL : {userRole}{" "}
                  </>
                ) : (
                  <>404 NOT FOUND</>
                )
              }
            />
            <>
            <Route
              path="/user"
              element={
                userRole === "user" ? (
                  <>
                    ¡USTED ES {username}, BIENVENIDO! SU ROL: {userRole}{" "}
                  </>
                ) : (
                  <>404 NOT FOUND</>
                )
              }
            />
            <>
            <Route
              path="/client"
              element={
                userRole === "client" ? (
                  <>
                    ¡USTED ES {username}, BIENVENIDO! SU ROL: {userRole}{" "}
                  </>
                ) : (
                  <>404 NOT FOUND</>
                )
              }
            />
          </>
        ) : (
          <Route path="/" element={<SignInPage />} />
        )}
        
        <Route path="/*" element={<>404 NOT FOUND</>} />
      </>
    )
  );

  return <RouterProvider router={router} />;
};

export default AppRouter;
