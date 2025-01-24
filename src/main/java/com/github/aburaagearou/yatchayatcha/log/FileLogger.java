package com.github.aburaagearou.yatchayatcha.log;

import com.github.aburaagearou.yatchayatcha.YatchaYatcha;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * オークション情報のファイルへのロギングクラス
 */
public class FileLogger implements IAuctionLogger {

	/**
	 * ロギング
	 * @param info オークション情報
	 */
	@Override
	public synchronized void log(AuctionInfo info) {
		// ログフォルダ
		File logDir = new File(YatchaYatcha.getInstance().getDataFolder(),"logs");
		if(!logDir.exists()) {
			if(!logDir.mkdirs()) {
				YatchaYatcha.getInstance().getLogger().warning("Failed to create log directory: " + logDir.getPath());
				return;
			}
		}

		// ログファイル
		String fileName = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
		File file = new File(logDir,fileName + ".log");
		try {
			if(!file.exists()) {
				if(!file.createNewFile()) {
					YatchaYatcha.getInstance().getLogger().warning("Failed to create log file: " + file.getPath());
					return;
				}
			}

			// ログ書き込み
			Files.write(file.toPath(), Collections.singletonList(info.getLogString()), StandardOpenOption.APPEND);

		} catch (IOException e) {
			YatchaYatcha.getInstance().getLogger().warning("Failed to create log file: " + file.getPath());
		}
	}
}
