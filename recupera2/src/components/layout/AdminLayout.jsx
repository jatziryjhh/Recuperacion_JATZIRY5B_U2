import React from 'react'
import { Outlet } from 'react-router-dom'

const AdminLayout = () => {
  return (
    <div>
        <header>
            NAVBAR
        </header>
        <aside> SIDEBAR</aside>
        <main> <Outlet></Outlet></main>
    </div>
  )
}

export default AdminLayout