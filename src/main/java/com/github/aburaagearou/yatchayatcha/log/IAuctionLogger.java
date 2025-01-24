package com.github.aburaagearou.yatchayatcha.log;

/**
 * オークションロギングの基底クラス
 */
public interface IAuctionLogger {

	/**
	 * ロギング
	 * @param info オークション情報
	 */
	void log(AuctionInfo info);
}
