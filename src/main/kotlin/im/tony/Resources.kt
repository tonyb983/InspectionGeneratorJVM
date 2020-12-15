package im.tony

public object Resources {
  public object Fonts {
    private const val prefix: String = "/fonts"
    public const val terminal: String = "$prefix/Terminal.TTF"
  }

  public object Creds {
    private const val prefix: String = "/creds"
    public const val credentials: String = "$prefix/credentials.json"
  }
}
