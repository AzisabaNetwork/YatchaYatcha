package com.github.aburaagearou.yatchayatcha.log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * オークション情報のメモリへのロギングクラス
 */
public class MemoryLogger implements IAuctionLogger {

	// ログ
	private static final List<AuctionInfo> logs = new ArrayList<>();

	/**
	 * ロギング
	 * @param info オークション情報
	 */
	@Override
	public void log(AuctionInfo info) {
		logs.add(info);
	}

	/**
	 * ログ文字列を取得
	 */
	public List<String> getColoredLog() {
		return logs.stream().map(AuctionInfo::getColoredLogString).collect(Collectors.toList());
	}
}
