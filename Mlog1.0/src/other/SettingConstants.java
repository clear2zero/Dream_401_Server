/*
 * 文件名: SettingConstants.java
 * 版本信息: 
 * 创建人: echo
 * 创建日期: Mar 14, 2007
 */
package other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 说明:
 * </P>
 * <p>
 * 描述:
 * </p>
 * <p>
 * 版权: Copyright(c)2006
 * </p>
 * <p>
 * 公司(团体): Hebca
 * </p>
 * 
 * @author echo
 * @version 1.0
 */
public class SettingConstants {

	public static String getDescription(String key) {
		Object o = map.get(key);
		if (o == null)
			return "";
		else
			return (String) o;
	}

	// mailbox
	public static final String mailbox_personalCapacity = "mailbox.common.personalCapacity";
	public static final String mailbox_attachmentSize = "mailbox.common.attachmentSize";
	public static final String mailbox_jasenUpdateURL = "mailbox.spam.jasenUpdateURL";
	public static final String mailbox_admins = "mailbox.admin.admins";

	public static final String mailbox_autoSign = "mailbox.common.autoSign";
	public static final String mailbox_signSeal = "mailbox.common.signSeal";
	public static final String mailbox_encrypt = "mailbox.common.encrypt";
	public static final String mailbox_sealMode = "mailbox.common.sealMode";
	public static final String mailbox_sealSigner = "mailbox.common.sealSigner";
	public static final String mailbox_locationDir = "mailbox.location.dir";

	public static final String netdisk_Capacity = "netdisk.common.personalCapacity";
	public static final String mailbox_encryptAlg = "mailbox.common.encryptAlg";

	public static final String mailbox_caMode = "mailbox.client.caMode";

	public static final String mailbox_loadTest = "mailbox.test.load";
	// 归档邮箱
	public static final String mailbox_autocc = "mailbox.autocc";
	// 默认信纸
	public static final String mailbox_moban_default = "mailbox.moban.default";

	// 系统自动发信邮箱账户
	public static final String mailbox_autoSendName = "mailbox.autosend.name";

	// 短信服务常量
	public static final String mailbox_sms_mode = "mailbox.sms.mode"; // 增加短信可配置选项
	public static final String mailbox_sms_provider = "mailbox.sms.provider";
	public static final String mailbox_sms_jindi = "mailbox.sms.jindi";
	public static final String mailbox_sms_virtual = "mailbox.sms.virtual";
	public static final String mailbox_sms_enabled = "mailbox.sms.enabled";
	public static final String mailbox_sms_userSmsEnabled = "mailbox.sms.userSmsEnabled";

	// 判断登录设备验证是否开启
	public static final String mailbox_checkdevice_enabled = "mailbox.checkdevice.enabled";

	// 推送服务常量
	public static final String mailbox_push_enabled = "mailbox.push.enabled";
	public static final String mailbox_push_default = "mailbox.push.default";

	// 用户注册常量
	public static final String mailbox_register_enabled = "mailbox.register.enabled";
	// 注册方式
	public static final String register_enabled_open = "1";// 开放注册
	public static final String register_enabled_halfopen = "2";// 半开放
	public static final String register_enabled_close = "3";// 关闭注册
	// 备份策略常量
	public static final String mailbox_backup_enabled = "mailbox.backup.enabled";
	public static final String mailbox_backup_period_month = "mailbox.backup.period.month";
	public static final String mailbox_backup_period_month_day = "mailbox.backup.period.month.day";
	public static final String mailbox_backup_period_month_hour = "mailbox.backup.period.month.hour";
	public static final String mailbox_backup_period_month_minute = "mailbox.backup.period.month.minute";
	public static final String mailbox_backup_period_month_second = "mailbox.backup.period.month.second";
	public static final String mailbox_backup_period_day = "mailbox.backup.period.day";
	public static final String mailbox_backup_period_day_hour = "mailbox.backup.period.day.hour";
	public static final String mailbox_backup_period_day_minute = "mailbox.backup.period.day.minute";
	public static final String mailbox_backup_period_day_second = "mailbox.backup.period.day.second";
	public static final String mailbox_backup_path = "mailbox.backup.path";
	public static final String mailbox_backup_uploadtoftp = "mailbox.backup.uploadtoftp";
	public static final String mailbox_backup_ftp_address = "mailbox.backup.ftp.address";
	public static final String mailbox_backup_ftp_user = "mailbox.backup.ftp.user";
	public static final String mailbox_backup_ftp_password = "mailbox.backup.ftp.password";
	public static final String mailbox_backup_ftp_deletefile = "mailbox.backup.ftp.deletefile";
	public static final String mailbox_clamav_update = "mailbox.clamav.update";

	public static final String mailbox_officeServer_dbConnectString = "mailbox.offficeServer.dbConnectString";

	public static final String home_foot_firstline = "home.foot.firstline";
	public static final String home_foot_secondline = "home.foot.secondline";
	public static final String home_foot_thirdline = "home.foot.thirdline";

	// 系统常量
	public static final String mailbox_system_name = "mailbox.system.name";
	// verify
	public static final String verify_type = "verify.type";
	public static final String verify_svsSocket = "verify.svs.socket";
	public static final String verify_svsIp = "verify.svs.ip";
	public static final String verify_svsPort = "verify.svs.port";

	public static final String verify_itemTime = "verify.item.time";
	public static final String verify_itemChain = "verify.item.chain";
	public static final String verify_itemStatus = "verify.item.status";
	public static final String verify_useCrl = "verify.use.crl";
	public static final String verify_useLdap = "verify.use.ldap";
	public static final String verify_rootFile = "verify.root.file";
	public static final String verify_ldapIp = "verify.ldap.ip";
	public static final String verify_ldapPort = "verify.ldap.port";
	public static final String verify_ldapUser = "verify.ldap.user";
	public static final String verify_ldapPassword = "verify.ldap.password";
	public static final String verify_crlFile = "verify.crl.file";

	public static final String verify_filterOugl = "verify.filter.ougl";
	public static final String verify_filterCngl = "verify.filter.cngl";

	// config
	public static final String config_shortname = "servers.server.shortname";
	public static final String config_fetchServer = "servers.server.fetch-server";
	public static final String config_fetchServerPort = "servers.server.fetch-server-port";
	public static final String config_fetchProtocol = "servers.server.fetch-protocol";
	public static final String config_smtpServer = "servers.server.smtp-server";
	public static final String config_smtpServerPort = "servers.server.smtp-server-port";
	public static final String config_smtpAuthenticated = "servers.server.smtp-authenticated";
	public static final String config_adminProvider = "servers.server.admin-provider";
	public static final String config_dbid = "db-config.db.id";
	public static final String config_database = "db-config.db.database";
	public static final String config_driver = "db-config.db.driver";
	public static final String config_login = "db-config.db.login";
	public static final String config_password = "db-config.db.password";
	public static final String config_deploy = "deploy-management.deploy";
	public static final String config_deployStep = "deploy-management.deploy-step";
	public static final String webConfig_session_timeout = "session-config.session-timeout";// add
																							// by
																							// shenqn

	public static final String config_netdiskModule = "support-modules.netdisk";
	public static final String config_sms = "support-modules.sms";
	public static final String config_chat = "support-modules.chat";
	public static final String config_phone = "support-modules.phone";

	public static final String config_multidomain_support = "multidomain.support";
	public static final String config_multidomain_cname = "multidomain.cname";
	public static final String config_multidomain_mx = "multidomain.mx";
	public static final String config_multidomain_priority = "multidomain.priority";

	public static final String jamesconfig_dbUrl = "database.connection.url";
	public static final String jamesconfig_dbDriver = "database.connection.driver";
	public static final String jamesconfig_dbUser = "database.connection.username";
	public static final String jamesconfig_dbPassword = "database.connection.password";

	public static final String postfixconfig_dbUrl = "database.connection.url";
	public static final String postfixconfig_dbDriver = "database.connection.driver";
	public static final String postfixconfig_dbUser = "database.connection.username";
	public static final String postfixconfig_dbPassword = "database.connection.password";
	public static final String postfixconfig_serverIp = "postfix.server.ip";
	public static final String postfixconfig_serverPort = "postfix.server.port";
	public static final String postfixconfig_serverAdmin = "postfix.server.aminuser";
	public static final String postfixconfig_serverAdminpass = "postfix.server.aminpass";
	public static final String postfixconfig_domain = "postfix.domain";

	public static final String qmailconfig_dbUrl = "database.connection.url";
	public static final String qmailconfig_dbDriver = "database.connection.driver";
	public static final String qmailconfig_dbUser = "database.connection.username";
	public static final String qmailconfig_dbPassword = "database.connection.password";

	// http端口
	public static final String mailbox_http_port = "mailbox.http.port";

	private static Map map = new HashMap();

	static {
		map.put(mailbox_personalCapacity, "容量");
		map.put(mailbox_attachmentSize, "附件大小");
		map.put(mailbox_autoSign, "是否默认签名");
		map.put(netdisk_Capacity, "网盘大小");

	}
	// 即时通讯
	public static final String chat_IP = "mailbox.chat.ip";
	public static final String chat_Port = "mailbox.chat.port";
	// 用户配置信息
	public static final String mailbox_template_setname = "mailbox.template.setname";
	// 是否强制签名
	public static final String mailbox_writeLetter_forceSign = "mailbox.writeLetter.forceSign";
	// 是否只能发内部邮箱
	public static final String mailbox_allowSendOut = "mailbox.allowSendOut";
	// 导航是否显示word模版
	public static final String mailbox_navbar_displayWord = "mailbox.navbar.displayWord";
	// 备份文件时的密码
	public static final String mailbox_backup_tar_password = "mailbox.backup.tar.password";
	// 超级管理员配置项 群发数量
	public static final String mailbox_send_groupsendnum = "mailbox.send.groupsendnum";
	// 单位默认邮箱容量
	public static final String unitDefaultCapacity = "mailbox.unitDefaultCapacity";
	// 每人邮箱默认容量
	public static final String userDefaultCapacity = "mailbox.userDefaultCapacity";
	// 单位默认网盘容量
	public static final String unitNetDiskCapacity = "mailbox.unitNetDiskCapacity";
	// 用户默认网盘容量
	public static final String userNetDiskCapacity = "mailbox.userNetDiskCapacity";
	// 默认可见性
	public static final String unituserVisible = "mailbox.unituserVisible";
	// 单位注册默认用户限制
	public static final String unitinitUserNum = "mailbox.unitinitUserNum";
	// 新邮件是否启用短信提醒
	public static final String newMailSmsRemind = "mailbox.sms.newMailSmsRemind";

	// 是否开启邮件检测线程,如果关闭无法收到短信提醒,此目的用于解决服务器有多个tomcat导致每个tomcat都会线程扫描 add by xj
	// 2012-11-29
	public static final String checkNewMail = "mailbox.checkNewMail";
	// 压力测试,调试等开关,去掉KEY登陆等 add by xj 2012-11-29
	public static final String debug = "mailbox.debug";

	// 系统皮肤设置 2013.05.20 gaobiao
	public static final String mailskin = "mailbox.skin";

	// 加密模式 add by shenqn
	public static final String mailbox_crypto_model = "mailbox.crypto.model";

	// 是否验证签章签名,可能 有旧签章无法通过验证，可以不验证
	public static final String mailbox_common_sealverify = "mailbox.common.sealverify";

	// 随机源提供者
	public static final String mail_randomProvider = "mail.randomProvider";

	/** 电话呼叫类型，目前为云通讯 add by xj 2014年3月10日 */
	public static String SYSCONFIG_PHONE_MODE = "sysconfig.phone.mode";
	/** 电话呼叫云通讯相关参数 add by xj 2014年3月10日 */
	public static final String YUNTX_REST_SERVER_ADDRESS = "sysconfig.phone.ytx.serverAddress";
	public static final String YUNTX_REST_SERVER_PORT = "sysconfig.phone.ytx.serverPort";
	public static final String YUNTX_MAIN_ACCOUNT = "sysconfig.phone.ytx.mainAccount";
	public static final String YUNTX_MAIN_TOKEN = "sysconfig.phone.ytx.mainToken";
	public static final String YUNTX_SUB_ACCOUNT = "sysconfig.phone.ytx.subAccount";
	public static final String YUNTX_SUB_TOKEN = "sysconfig.phone.ytx.subToken";
	public static final String YUNTX_SUB_NAME = "sysconfig.phone.ytx.subName";
	public static final String YUNTX_VOIP_ID = "sysconfig.phone.ytx.voipAccount";
	public static final String YUNTX_VOIP_PWD = "sysconfig.phone.ytx.voipPassword";
	public static final String YUNTX_APP_ID = "sysconfig.phone.ytx.appId";

	/** 是否开启缓冲 add by xj 2014年3月17日 */
	public static final String CACHE_MEMCACHED_OPEN = "mailbox.memcached.open";
	public static final String CACHE_MEMCACHED_IPS = "mailbox.memcached.ips";
	public static final String CACHE_MEMCACHED_PORT = "mailbox.memcached.port";

	public static final String SYS_PHONECOSTPM = "sysconfig.phone.phonecostpm";	
	
	//系统风格模式,MAIL或者OA
	public static final String SYSTEM_STYLE_MODE="system.style.mode";
	public static final String MAIL="mail";
	public static final String OA ="oa";
	
	// 是否显示域名
	public static final String mailbox_contacts_isDisplayDomain = "mailbox.contacts.isDisplayDomain";
}
