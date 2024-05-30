package service;

public enum Permission {
	ADMIN,
    READ,
    WRITE,
    RATE,
    EDIT,
    DELETE,
	TEST;

    public static boolean canRead(Permission[] ps) {
        for (Permission p : ps) {
            if (p == READ)
                return true;
        }
        return false;
    }
    
    public static boolean isAdmin(Permission[] ps) {
        try {
            for (Permission p : ps) {
                if (p == ADMIN)
                    return true;
            }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean canWrite(Permission[] ps) {
        for (Permission p : ps) {
            if (p == WRITE)
                return true;
        }
        return false;
    }

    public static boolean canRate(Permission[] ps) {
        for (Permission p : ps) {
            if (p == RATE)
                return true;
        }
        return false;
    }

    public static boolean canEdit(Permission[] ps) {
        for (Permission p : ps) {
            if (p == EDIT)
                return true;
        }
        return false;
    }

    public static boolean canDelete(Permission[] ps) {
        for (Permission p : ps) {
            if (p == DELETE)
                return true;
        }
        return false;
    }
    
    public static boolean canTest(Permission[] ps) {
        for (Permission p : ps) {
            if (p == TEST)
                return true;
        }
        return false;
    }
}