# cjdb

Spent about 3 days at my full role working on migration of data from a legacy
application that we were replacing.

I have been involved in quite a few migration projects, and one good aspect of
them is that they're not necessarily tied to the tech stack being used.
I've used tck/tk and ruby in the past, and have used clojure at work to write
some development related tools.

I'm currently working on a contract, looking after an oldish java web app.
The codebase had no unit testing and the developers don't seem to have come
across the concept of database transactions.

I implemented 'some' unit tests including some using an in memory database.

Currently the schema for that database has been hand-written, and I hope to
at least generate something here that will automate that. (It is currently a bit
behind the real db schema)


This is a collection of tools that might be of use in future migration,
relational database type tools.


## setting up mbrainz database (postgres)
either download a vm or database dump file. I did the latter.

createdb --encoding=UNICODE mbrainz
psql mbrainz
will come back to this.

Nah, will use the virtualbox vm.....

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar cjdb-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
