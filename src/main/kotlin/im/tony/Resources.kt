package im.tony

public object Resources {
  public object Data {
    private const val prefix: String = "/data"
    public const val drive: String = "$prefix/drive.txt"
    public const val owned: String = "$prefix/owned.txt"
  }

  public object Fonts {
    private const val prefix: String = "/fonts"
    public const val terminal: String = "$prefix/Terminal.TTF"
  }

  public object Creds {
    private const val prefix: String = "/creds"
    public const val credentials: String = "$prefix/credentials.json"
  }
}
