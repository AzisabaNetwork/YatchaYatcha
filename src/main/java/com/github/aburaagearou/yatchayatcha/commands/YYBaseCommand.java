package com.github.aburaagearou.yatchayatcha.commands;

import co.aikar.commands.BaseCommand;
import com.github.aburaagearou.yatchayatcha.YatchaYatcha;

/**
 * YatchaYatchaのコマンドの基底クラス
 * @author AburaAgeTarou
 */
public abstract class YYBaseCommand extends BaseCommand {

	/**
	 * コンストラクタ
	 * メインクラスのコマンドリストに登録
	 */
	public YYBaseCommand() {
		YatchaYatcha.addCommand(this);
	}
}
