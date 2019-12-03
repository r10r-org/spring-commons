# Spring Commons - Usability tools for Spring [![Build Status](https://api.travis-ci.org/r10r-org/spring-commons.svg)](https://travis-ci.org/r10r-org/spring-commons)


## TransactionHelper
The @Transaction annotation in Spring has two big shortcomings.

1. They only rollback if they are configured via (rollbackFor = Throwable.class). Otherwise checked exceptions
   won't roll back the exception. This might be really unexpected.
2. Transactions might be inactive if placed on "private" methods, or if methods are not executed from another bean.
   This is really error prone as transactions might not be active even though annotations are there. Spring won't
   warn about that. Nothing. Ouch.

The TransactionHelper might help you in that case as it makes it explicit that there's a transaction. And it is less
magic as it always rolls back on any exception.

Your mileage may vary.
Especially the forced definition of return types is not really nice (transactionHelper.<MyException>runI...).

This TransactionHelper can be injected into your class and the simply used via:

    transactionHelper.runInTransaction(() ->
      // ... your code
    )};

If you want to throw checked exception it gets a bit ugly as you have to specify them like that:


    transactionHelper.<MyException>runInTransactionAndThrow(() ->
      // ... your code
    )};


Less error-prone and it is more clear what happens. 

Note: There's also Spring's TransactionTemplate, 
but it does not have support for checked exceptions..


# Releasing (committers only)

Make sure you got gpg installed on your machine. Gpg is as good as gpg2, so
there's not much difference. But the gpg plugin in maven works better with gpg,
so we go with that one

    brew install gpg

Make sure to create a key

    gpg --gen-key

Then list the keys and send the public key to a keyserver so that people can
verify that it's you:

    gpg --keyserver hkp://pool.sks-keyservers.net --send-keys YOUR_PUBLIC_KEY

Make sure to set 

    export GPG_TTY=$(tty)

... that way any input of gpg will be properly shown (entering your passphrase for instance)...

Make sure you set the sonatype credentials in your ~/.m2/settings.xml:

```
<settings>

  <servers>
    <server>
      <id>ossrh</id>
      <username>USERNAME</username>
      <password>PASSWORD</password>
    </server>
  </servers>

</settings>
```


Then you can create  a new release like so:

    mvn release:clean -Prelease
    mvn release:prepare -Prelease
    mvn release:perform -Prelease
