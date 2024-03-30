package noctiluca.features.shared

internal const val SIMPLE_HTML_PARAGRAPH_1 = "<p>吾輩は猫である。名前はまだない。</p>"
internal const val SIMPLE_TEXT_PARAGRAPH_1 = "吾輩は猫である。名前はまだない。\n"

internal const val SIMPLE_HTML_PARAGRAPH_2 =
    "<p>一人の下人が、羅生門の下で雨やみを待っていた。<br>広い門の下には、この男の外に誰もいない。</p>"
internal const val SIMPLE_TEXT_PARAGRAPH_2 =
    "一人の下人が、羅生門の下で雨やみを待っていた。\n広い門の下には、この男の外に誰もいない。\n"

internal val WITH_LINK_HTML_PARAGRAPH = """
    |<p>
      |東京で桜開花 過去10年で最も遅く<br>
      |<a
        | href="https://news.yahoo.co.jp/articles/258a461f46892bf6908b1abfa5e92d30f188c9d2"
        | target="_blank"
        | rel="nofollow noopener noreferrer"
        | translate="no"
        | class="status-link unhandled-link"
        | title="https://news.yahoo.co.jp/articles/258a461f46892bf6908b1abfa5e92d30f188c9d2"
      |>
        |<span class="invisible">https://</span>
        |<span class="ellipsis">news.yahoo.co.jp/articles/258a</span>
        |<span class="invisible">461f46892bf6908b1abfa5e92d30f188c9d2</span>
      |</a>
    |</p>
""".trimMargin().replace("[\n\r]".toRegex(), "")

internal const val WITH_LINK_TEXT_PARAGRAPH =
    "東京で桜開花 過去10年で最も遅く\nnews.yahoo.co.jp/articles/258a...\n"

internal val WITH_HASH_TAG_HTML_PARAGRAPH = """
    |<p>
      |【2024年1月1日の天気】曇りのち晴れ
      |<a href="/tags/今日の天気" class="mention hashtag status-link" rel="tag">#<span>今日の天気</span></a>
    |</p>
""".trimMargin().replace("[\n\r]".toRegex(), "")
internal const val WITH_HASH_TAG_TEXT_PARAGRAPH = "【2024年1月1日の天気】曇りのち晴れ#今日の天気\n"

internal const val ENCODED_HTML_PARAGRAPH =
    "<p>xx@MacBook-Air ~ % brew info xz</p><p>==&gt; xz: stable 5.6.1 (bottled)</p>"
internal const val ENCODED_TEXT_PARAGRAPH =
    "xx@MacBook-Air ~ % brew info xz\n==> xz: stable 5.6.1 (bottled)\n"
