package im.tony.google.extensions.drive

/*
contains	The content of one string is present in the other.
  =	        The content of a string or boolean is equal to the other.
  !=        The content of a string or boolean is not equal to the other.
  <	        A value is less than another.
  <=        A value is less than or equal to another.
  >	        A value is greater than another.
  >=        A value is greater than or equal to another.
  in        An element is contained within a collection.
  and       Return items that match both queries.
  or        Return items that match either query.
  not       Negates a search query.
  has       A collection contains an element matching the parameters.
 */

public enum class QueryOperators {
  Contains,
  Eq,
  NotEq,
  Lt,
  Lte,
  Gt,
  Gte,
  In,
  And,
  Or,
  Not,
  Has;

  override fun toString(): String = when (this) {
    Contains -> "contains"
    Eq -> "="
    NotEq -> "!="
    Lt -> "<"
    Lte -> "<="
    Gt -> ">"
    Gte -> ">="
    In -> "in"
    And -> "and"
    Or -> "or"
    Not -> "not"
    Has -> "has"
  }
}
