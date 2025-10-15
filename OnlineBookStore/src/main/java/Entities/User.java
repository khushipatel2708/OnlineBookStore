/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author 91931
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByHost", query = "SELECT u FROM User u WHERE u.host = :host"),
    @NamedQuery(name = "User.findByUser", query = "SELECT u FROM User u WHERE u.userPK.user = :user"),
    @NamedQuery(name = "User.findBySelectpriv", query = "SELECT u FROM User u WHERE u.selectpriv = :selectpriv"),
    @NamedQuery(name = "User.findByInsertpriv", query = "SELECT u FROM User u WHERE u.insertpriv = :insertpriv"),
    @NamedQuery(name = "User.findByUpdatepriv", query = "SELECT u FROM User u WHERE u.updatepriv = :updatepriv"),
    @NamedQuery(name = "User.findByDeletepriv", query = "SELECT u FROM User u WHERE u.deletepriv = :deletepriv"),
    @NamedQuery(name = "User.findByCreatepriv", query = "SELECT u FROM User u WHERE u.createpriv = :createpriv"),
    @NamedQuery(name = "User.findByDroppriv", query = "SELECT u FROM User u WHERE u.droppriv = :droppriv"),
    @NamedQuery(name = "User.findByReloadpriv", query = "SELECT u FROM User u WHERE u.reloadpriv = :reloadpriv"),
    @NamedQuery(name = "User.findByShutdownpriv", query = "SELECT u FROM User u WHERE u.shutdownpriv = :shutdownpriv"),
    @NamedQuery(name = "User.findByProcesspriv", query = "SELECT u FROM User u WHERE u.processpriv = :processpriv"),
    @NamedQuery(name = "User.findByFilepriv", query = "SELECT u FROM User u WHERE u.filepriv = :filepriv"),
    @NamedQuery(name = "User.findByGrantpriv", query = "SELECT u FROM User u WHERE u.grantpriv = :grantpriv"),
    @NamedQuery(name = "User.findByReferencespriv", query = "SELECT u FROM User u WHERE u.referencespriv = :referencespriv"),
    @NamedQuery(name = "User.findByIndexpriv", query = "SELECT u FROM User u WHERE u.indexpriv = :indexpriv"),
    @NamedQuery(name = "User.findByAlterpriv", query = "SELECT u FROM User u WHERE u.alterpriv = :alterpriv"),
    @NamedQuery(name = "User.findByShowdbpriv", query = "SELECT u FROM User u WHERE u.showdbpriv = :showdbpriv"),
    @NamedQuery(name = "User.findBySuperpriv", query = "SELECT u FROM User u WHERE u.superpriv = :superpriv"),
    @NamedQuery(name = "User.findByCreatetmptablepriv", query = "SELECT u FROM User u WHERE u.createtmptablepriv = :createtmptablepriv"),
    @NamedQuery(name = "User.findByLocktablespriv", query = "SELECT u FROM User u WHERE u.locktablespriv = :locktablespriv"),
    @NamedQuery(name = "User.findByExecutepriv", query = "SELECT u FROM User u WHERE u.executepriv = :executepriv"),
    @NamedQuery(name = "User.findByReplslavepriv", query = "SELECT u FROM User u WHERE u.replslavepriv = :replslavepriv"),
    @NamedQuery(name = "User.findByReplclientpriv", query = "SELECT u FROM User u WHERE u.replclientpriv = :replclientpriv"),
    @NamedQuery(name = "User.findByCreateviewpriv", query = "SELECT u FROM User u WHERE u.createviewpriv = :createviewpriv"),
    @NamedQuery(name = "User.findByShowviewpriv", query = "SELECT u FROM User u WHERE u.showviewpriv = :showviewpriv"),
    @NamedQuery(name = "User.findByCreateroutinepriv", query = "SELECT u FROM User u WHERE u.createroutinepriv = :createroutinepriv"),
    @NamedQuery(name = "User.findByAlterroutinepriv", query = "SELECT u FROM User u WHERE u.alterroutinepriv = :alterroutinepriv"),
    @NamedQuery(name = "User.findByCreateuserpriv", query = "SELECT u FROM User u WHERE u.createuserpriv = :createuserpriv"),
    @NamedQuery(name = "User.findByEventpriv", query = "SELECT u FROM User u WHERE u.eventpriv = :eventpriv"),
    @NamedQuery(name = "User.findByTriggerpriv", query = "SELECT u FROM User u WHERE u.triggerpriv = :triggerpriv"),
    @NamedQuery(name = "User.findByCreatetablespacepriv", query = "SELECT u FROM User u WHERE u.createtablespacepriv = :createtablespacepriv"),
    @NamedQuery(name = "User.findBySslType", query = "SELECT u FROM User u WHERE u.sslType = :sslType"),
    @NamedQuery(name = "User.findByMaxQuestions", query = "SELECT u FROM User u WHERE u.maxQuestions = :maxQuestions"),
    @NamedQuery(name = "User.findByMaxUpdates", query = "SELECT u FROM User u WHERE u.maxUpdates = :maxUpdates"),
    @NamedQuery(name = "User.findByMaxConnections", query = "SELECT u FROM User u WHERE u.maxConnections = :maxConnections"),
    @NamedQuery(name = "User.findByMaxUserConnections", query = "SELECT u FROM User u WHERE u.maxUserConnections = :maxUserConnections"),
    @NamedQuery(name = "User.findByPlugin", query = "SELECT u FROM User u WHERE u.plugin = :plugin"),
    @NamedQuery(name = "User.findByPasswordExpired", query = "SELECT u FROM User u WHERE u.passwordExpired = :passwordExpired"),
    @NamedQuery(name = "User.findByPasswordLastChanged", query = "SELECT u FROM User u WHERE u.passwordLastChanged = :passwordLastChanged"),
    @NamedQuery(name = "User.findByPasswordLifetime", query = "SELECT u FROM User u WHERE u.passwordLifetime = :passwordLifetime"),
    @NamedQuery(name = "User.findByAccountLocked", query = "SELECT u FROM User u WHERE u.accountLocked = :accountLocked"),
    @NamedQuery(name = "User.findByCreaterolepriv", query = "SELECT u FROM User u WHERE u.createrolepriv = :createrolepriv"),
    @NamedQuery(name = "User.findByDroprolepriv", query = "SELECT u FROM User u WHERE u.droprolepriv = :droprolepriv"),
    @NamedQuery(name = "User.findByPasswordreusehistory", query = "SELECT u FROM User u WHERE u.passwordreusehistory = :passwordreusehistory"),
    @NamedQuery(name = "User.findByPasswordreusetime", query = "SELECT u FROM User u WHERE u.passwordreusetime = :passwordreusetime"),
    @NamedQuery(name = "User.findByPasswordrequirecurrent", query = "SELECT u FROM User u WHERE u.passwordrequirecurrent = :passwordrequirecurrent"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.userPK.id = :id"),
    @NamedQuery(name = "User.findByFullname", query = "SELECT u FROM User u WHERE u.fullname = :fullname"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserPK userPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Host")
    private String host;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Select_priv")
    private Character selectpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Insert_priv")
    private Character insertpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Update_priv")
    private Character updatepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Delete_priv")
    private Character deletepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_priv")
    private Character createpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Drop_priv")
    private Character droppriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Reload_priv")
    private Character reloadpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Shutdown_priv")
    private Character shutdownpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Process_priv")
    private Character processpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "File_priv")
    private Character filepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Grant_priv")
    private Character grantpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "References_priv")
    private Character referencespriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Index_priv")
    private Character indexpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Alter_priv")
    private Character alterpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Show_db_priv")
    private Character showdbpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Super_priv")
    private Character superpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_tmp_table_priv")
    private Character createtmptablepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Lock_tables_priv")
    private Character locktablespriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Execute_priv")
    private Character executepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Repl_slave_priv")
    private Character replslavepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Repl_client_priv")
    private Character replclientpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_view_priv")
    private Character createviewpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Show_view_priv")
    private Character showviewpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_routine_priv")
    private Character createroutinepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Alter_routine_priv")
    private Character alterroutinepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_user_priv")
    private Character createuserpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Event_priv")
    private Character eventpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Trigger_priv")
    private Character triggerpriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_tablespace_priv")
    private Character createtablespacepriv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "ssl_type")
    private String sslType;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "ssl_cipher")
    private byte[] sslCipher;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "x509_issuer")
    private byte[] x509Issuer;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "x509_subject")
    private byte[] x509Subject;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_questions")
    private int maxQuestions;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_updates")
    private int maxUpdates;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_connections")
    private int maxConnections;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_user_connections")
    private int maxUserConnections;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "plugin")
    private String plugin;
    @Lob
    @Size(max = 65535)
    @Column(name = "authentication_string")
    private String authenticationString;
    @Basic(optional = false)
    @NotNull
    @Column(name = "password_expired")
    private Character passwordExpired;
    @Column(name = "password_last_changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordLastChanged;
    @Column(name = "password_lifetime")
    private Short passwordLifetime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "account_locked")
    private Character accountLocked;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Create_role_priv")
    private Character createrolepriv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Drop_role_priv")
    private Character droprolepriv;
    @Column(name = "Password_reuse_history")
    private Short passwordreusehistory;
    @Column(name = "Password_reuse_time")
    private Short passwordreusetime;
    @Column(name = "Password_require_current")
    private Character passwordrequirecurrent;
    @Lob
    @Size(max = 1073741824)
    @Column(name = "User_attributes")
    private String userattributes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Fullname")
    private String fullname;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 15)
    @Column(name = "Phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Username")
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Password")
    private String password;
    @Size(max = 8)
    @Column(name = "Status")
    private String status;
    @JoinColumn(name = "Group_id", referencedColumnName = "Group_id")
    @ManyToOne(optional = false)
    private GroupMaster groupid;

    public User() {
    }

    public User(UserPK userPK) {
        this.userPK = userPK;
    }

    public User(UserPK userPK, String host, Character selectpriv, Character insertpriv, Character updatepriv, Character deletepriv, Character createpriv, Character droppriv, Character reloadpriv, Character shutdownpriv, Character processpriv, Character filepriv, Character grantpriv, Character referencespriv, Character indexpriv, Character alterpriv, Character showdbpriv, Character superpriv, Character createtmptablepriv, Character locktablespriv, Character executepriv, Character replslavepriv, Character replclientpriv, Character createviewpriv, Character showviewpriv, Character createroutinepriv, Character alterroutinepriv, Character createuserpriv, Character eventpriv, Character triggerpriv, Character createtablespacepriv, String sslType, byte[] sslCipher, byte[] x509Issuer, byte[] x509Subject, int maxQuestions, int maxUpdates, int maxConnections, int maxUserConnections, String plugin, Character passwordExpired, Character accountLocked, Character createrolepriv, Character droprolepriv, String fullname, String username, String email, String password) {
        this.userPK = userPK;
        this.host = host;
        this.selectpriv = selectpriv;
        this.insertpriv = insertpriv;
        this.updatepriv = updatepriv;
        this.deletepriv = deletepriv;
        this.createpriv = createpriv;
        this.droppriv = droppriv;
        this.reloadpriv = reloadpriv;
        this.shutdownpriv = shutdownpriv;
        this.processpriv = processpriv;
        this.filepriv = filepriv;
        this.grantpriv = grantpriv;
        this.referencespriv = referencespriv;
        this.indexpriv = indexpriv;
        this.alterpriv = alterpriv;
        this.showdbpriv = showdbpriv;
        this.superpriv = superpriv;
        this.createtmptablepriv = createtmptablepriv;
        this.locktablespriv = locktablespriv;
        this.executepriv = executepriv;
        this.replslavepriv = replslavepriv;
        this.replclientpriv = replclientpriv;
        this.createviewpriv = createviewpriv;
        this.showviewpriv = showviewpriv;
        this.createroutinepriv = createroutinepriv;
        this.alterroutinepriv = alterroutinepriv;
        this.createuserpriv = createuserpriv;
        this.eventpriv = eventpriv;
        this.triggerpriv = triggerpriv;
        this.createtablespacepriv = createtablespacepriv;
        this.sslType = sslType;
        this.sslCipher = sslCipher;
        this.x509Issuer = x509Issuer;
        this.x509Subject = x509Subject;
        this.maxQuestions = maxQuestions;
        this.maxUpdates = maxUpdates;
        this.maxConnections = maxConnections;
        this.maxUserConnections = maxUserConnections;
        this.plugin = plugin;
        this.passwordExpired = passwordExpired;
        this.accountLocked = accountLocked;
        this.createrolepriv = createrolepriv;
        this.droprolepriv = droprolepriv;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String user, int id) {
        this.userPK = new UserPK(user, id);
    }

    public UserPK getUserPK() {
        return userPK;
    }

    public void setUserPK(UserPK userPK) {
        this.userPK = userPK;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Character getSelectpriv() {
        return selectpriv;
    }

    public void setSelectpriv(Character selectpriv) {
        this.selectpriv = selectpriv;
    }

    public Character getInsertpriv() {
        return insertpriv;
    }

    public void setInsertpriv(Character insertpriv) {
        this.insertpriv = insertpriv;
    }

    public Character getUpdatepriv() {
        return updatepriv;
    }

    public void setUpdatepriv(Character updatepriv) {
        this.updatepriv = updatepriv;
    }

    public Character getDeletepriv() {
        return deletepriv;
    }

    public void setDeletepriv(Character deletepriv) {
        this.deletepriv = deletepriv;
    }

    public Character getCreatepriv() {
        return createpriv;
    }

    public void setCreatepriv(Character createpriv) {
        this.createpriv = createpriv;
    }

    public Character getDroppriv() {
        return droppriv;
    }

    public void setDroppriv(Character droppriv) {
        this.droppriv = droppriv;
    }

    public Character getReloadpriv() {
        return reloadpriv;
    }

    public void setReloadpriv(Character reloadpriv) {
        this.reloadpriv = reloadpriv;
    }

    public Character getShutdownpriv() {
        return shutdownpriv;
    }

    public void setShutdownpriv(Character shutdownpriv) {
        this.shutdownpriv = shutdownpriv;
    }

    public Character getProcesspriv() {
        return processpriv;
    }

    public void setProcesspriv(Character processpriv) {
        this.processpriv = processpriv;
    }

    public Character getFilepriv() {
        return filepriv;
    }

    public void setFilepriv(Character filepriv) {
        this.filepriv = filepriv;
    }

    public Character getGrantpriv() {
        return grantpriv;
    }

    public void setGrantpriv(Character grantpriv) {
        this.grantpriv = grantpriv;
    }

    public Character getReferencespriv() {
        return referencespriv;
    }

    public void setReferencespriv(Character referencespriv) {
        this.referencespriv = referencespriv;
    }

    public Character getIndexpriv() {
        return indexpriv;
    }

    public void setIndexpriv(Character indexpriv) {
        this.indexpriv = indexpriv;
    }

    public Character getAlterpriv() {
        return alterpriv;
    }

    public void setAlterpriv(Character alterpriv) {
        this.alterpriv = alterpriv;
    }

    public Character getShowdbpriv() {
        return showdbpriv;
    }

    public void setShowdbpriv(Character showdbpriv) {
        this.showdbpriv = showdbpriv;
    }

    public Character getSuperpriv() {
        return superpriv;
    }

    public void setSuperpriv(Character superpriv) {
        this.superpriv = superpriv;
    }

    public Character getCreatetmptablepriv() {
        return createtmptablepriv;
    }

    public void setCreatetmptablepriv(Character createtmptablepriv) {
        this.createtmptablepriv = createtmptablepriv;
    }

    public Character getLocktablespriv() {
        return locktablespriv;
    }

    public void setLocktablespriv(Character locktablespriv) {
        this.locktablespriv = locktablespriv;
    }

    public Character getExecutepriv() {
        return executepriv;
    }

    public void setExecutepriv(Character executepriv) {
        this.executepriv = executepriv;
    }

    public Character getReplslavepriv() {
        return replslavepriv;
    }

    public void setReplslavepriv(Character replslavepriv) {
        this.replslavepriv = replslavepriv;
    }

    public Character getReplclientpriv() {
        return replclientpriv;
    }

    public void setReplclientpriv(Character replclientpriv) {
        this.replclientpriv = replclientpriv;
    }

    public Character getCreateviewpriv() {
        return createviewpriv;
    }

    public void setCreateviewpriv(Character createviewpriv) {
        this.createviewpriv = createviewpriv;
    }

    public Character getShowviewpriv() {
        return showviewpriv;
    }

    public void setShowviewpriv(Character showviewpriv) {
        this.showviewpriv = showviewpriv;
    }

    public Character getCreateroutinepriv() {
        return createroutinepriv;
    }

    public void setCreateroutinepriv(Character createroutinepriv) {
        this.createroutinepriv = createroutinepriv;
    }

    public Character getAlterroutinepriv() {
        return alterroutinepriv;
    }

    public void setAlterroutinepriv(Character alterroutinepriv) {
        this.alterroutinepriv = alterroutinepriv;
    }

    public Character getCreateuserpriv() {
        return createuserpriv;
    }

    public void setCreateuserpriv(Character createuserpriv) {
        this.createuserpriv = createuserpriv;
    }

    public Character getEventpriv() {
        return eventpriv;
    }

    public void setEventpriv(Character eventpriv) {
        this.eventpriv = eventpriv;
    }

    public Character getTriggerpriv() {
        return triggerpriv;
    }

    public void setTriggerpriv(Character triggerpriv) {
        this.triggerpriv = triggerpriv;
    }

    public Character getCreatetablespacepriv() {
        return createtablespacepriv;
    }

    public void setCreatetablespacepriv(Character createtablespacepriv) {
        this.createtablespacepriv = createtablespacepriv;
    }

    public String getSslType() {
        return sslType;
    }

    public void setSslType(String sslType) {
        this.sslType = sslType;
    }

    public byte[] getSslCipher() {
        return sslCipher;
    }

    public void setSslCipher(byte[] sslCipher) {
        this.sslCipher = sslCipher;
    }

    public byte[] getX509Issuer() {
        return x509Issuer;
    }

    public void setX509Issuer(byte[] x509Issuer) {
        this.x509Issuer = x509Issuer;
    }

    public byte[] getX509Subject() {
        return x509Subject;
    }

    public void setX509Subject(byte[] x509Subject) {
        this.x509Subject = x509Subject;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public void setMaxQuestions(int maxQuestions) {
        this.maxQuestions = maxQuestions;
    }

    public int getMaxUpdates() {
        return maxUpdates;
    }

    public void setMaxUpdates(int maxUpdates) {
        this.maxUpdates = maxUpdates;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxUserConnections() {
        return maxUserConnections;
    }

    public void setMaxUserConnections(int maxUserConnections) {
        this.maxUserConnections = maxUserConnections;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getAuthenticationString() {
        return authenticationString;
    }

    public void setAuthenticationString(String authenticationString) {
        this.authenticationString = authenticationString;
    }

    public Character getPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(Character passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public Date getPasswordLastChanged() {
        return passwordLastChanged;
    }

    public void setPasswordLastChanged(Date passwordLastChanged) {
        this.passwordLastChanged = passwordLastChanged;
    }

    public Short getPasswordLifetime() {
        return passwordLifetime;
    }

    public void setPasswordLifetime(Short passwordLifetime) {
        this.passwordLifetime = passwordLifetime;
    }

    public Character getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Character accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Character getCreaterolepriv() {
        return createrolepriv;
    }

    public void setCreaterolepriv(Character createrolepriv) {
        this.createrolepriv = createrolepriv;
    }

    public Character getDroprolepriv() {
        return droprolepriv;
    }

    public void setDroprolepriv(Character droprolepriv) {
        this.droprolepriv = droprolepriv;
    }

    public Short getPasswordreusehistory() {
        return passwordreusehistory;
    }

    public void setPasswordreusehistory(Short passwordreusehistory) {
        this.passwordreusehistory = passwordreusehistory;
    }

    public Short getPasswordreusetime() {
        return passwordreusetime;
    }

    public void setPasswordreusetime(Short passwordreusetime) {
        this.passwordreusetime = passwordreusetime;
    }

    public Character getPasswordrequirecurrent() {
        return passwordrequirecurrent;
    }

    public void setPasswordrequirecurrent(Character passwordrequirecurrent) {
        this.passwordrequirecurrent = passwordrequirecurrent;
    }

    public String getUserattributes() {
        return userattributes;
    }

    public void setUserattributes(String userattributes) {
        this.userattributes = userattributes;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GroupMaster getGroupid() {
        return groupid;
    }

    public void setGroupid(GroupMaster groupid) {
        this.groupid = groupid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userPK != null ? userPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userPK == null && other.userPK != null) || (this.userPK != null && !this.userPK.equals(other.userPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.User[ userPK=" + userPK + " ]";
    }
    
}
