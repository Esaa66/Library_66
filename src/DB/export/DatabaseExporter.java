package DB.export;

import java.io.File;
import java.sql.CallableStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Alert.AlertMaker;
import javafx.concurrent.Task;
import DB.databaseHandlar;
import util.LibraryAssistantUtil;

/**
 *
 * @author Ahmad
 */
public class DatabaseExporter extends Task<Boolean> {

    private final File backupDirectory;

    public DatabaseExporter(File backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    protected Boolean call() {
        try {
            createBackup();
            return true;
        } catch (Exception exp) {
            AlertMaker.showErrorMessage(exp);
        }
        return false;
    }

    private void createBackup() throws Exception {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss");
        String backupdirectory = backupDirectory.getAbsolutePath() + File.separator + LocalDateTime.now().format(dateFormat);
        try (CallableStatement cs = databaseHandlar.DatabaseHandler.getInstance().getConnection().prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)")) {
            cs.setString(1, backupdirectory);
            cs.execute();
        }
        File file = new File(backupdirectory);
        LibraryAssistantUtil.openFileWithDesktop(file);
    }
}
