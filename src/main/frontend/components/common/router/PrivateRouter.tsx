'use client';

import React, { useEffect, useState } from "react";
import AxiosJwtInstance from "../instance/AxiosJwtInstance";
import { usePathname, useRouter } from 'next/navigation';

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [authorized, setAuthorized] = useState<boolean | null>(null);
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    const checkAuthorization = async () => {
      try {
        console.log( pathname );
        const response = await AxiosJwtInstance.post("/api/v1/auth/authorize", JSON.stringify({pathname}))
        setAuthorized(response.data);
      } catch (error) {
        console.error("Authorization check failed:", error);
        setAuthorized(false);
      }
    };

    checkAuthorization();
  }, [pathname]);

  if (authorized === null) {
    return <div>Loading...</div>;
  }

  if (!authorized) {
    return <div>Access Denied</div>;
  }

  return <>{children}</>;
};

export default PrivateRoute;
