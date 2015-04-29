package sto.common.util;

public enum RoleType{
	ADMIN {public String getName(){return "sysadmin";}},
	UNITADMIN {public String getName(){return "unitmanager";}},
	NORMAL {public String getName(){return "normaluser";}},
	UNIT_USERGROUP {public String getName(){return "unitmanager,deptmanager,normaluser";}},
	UNUNIT_USERGROUP {public String getName(){return "sysadmin,sysoperator,sysauditor";}},
	NOREPEAT_USERGROUP {public String getName(){return "sysadmin,sysoperator,sysauditor,unitmanager";}};
    public abstract String getName();
}
