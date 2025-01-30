# ヤッチャ！ヤッチャ！今夜は運オクだ！
運営オークション処理プラグイン
`/adminauction rule`でルール説明流して`/adminauction start`で商品出すだけ

## オークションの流れ
- 専用チャンネル「LGWAuction」への誘導、運オクのルール説明
  - `/adminauction rule`コマンドでルール説明を流せる
  - ルール見てないやつがいたら理解できるまで流そう

- 商品を運営オークションに出す流れ
  1. ［オークション開始］出品物を持って`/adminauction start`
    - 初期価格は何も引数を入れなければ$100から。`start`の後に数字を入れれば指定可能
  1. ［競り］誰も入札しなくなるまで競り
    - 参加者が`/bid 値段`を行い入札
    - 25秒間誰も入札しなければ落札
  1. ［落札］落札者に自動でアイテムを渡し、金額を回収
    - 落札者の金が移動したとかで足りない場合は無効になります

## 落札ログ
`/adminauction log`で前回起動からの落札ログをゲーム内に表示
全ての落札記録は`plugins/YatchaYatcha/logs/`に日付ごとに保存

## 設定可能項目
- カウントダウンの秒数
- 入札の最低価格
  - 元値への倍率
  - 元値への加算値
- ルール説明の定型文
- 効果音（CrackShot方式指定）
  - オークション開始
  - 入札（自分/他人）
  - カウントダウン
  - 落札
