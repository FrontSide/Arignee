[![Stories in Ready](https://badge.waffle.io/FrontSide/Arignee.png?label=ready&title=Ready)](https://waffle.io/FrontSide/Arignee) [![Build Status](https://travis-ci.org/FrontSide/Arignee.svg?branch=master)](https://travis-ci.org/FrontSide/Arignee)

Arignée
=======

A Web-Application for automated evaluations of e-commerce websites.

Background
==========

This Application is Part of my Bachelor Thesis I for the Bachelor of Science in Computing Degree Programme.

You can get some information on my [Bachelor I Blog](www.dary.info/blog/bac1).

API
===

Arignee uses a ReST-API witch which Website-evaluations can be triggered and evaluation results can be retained.

**All responses from the API are in JSON Objects/Arrays**

I use a ticketing system that takes advantage of the non-blocking I/O of Play!
Get some more information about that on my Blog!

| You request                |     You get |
|:---------------------------|------------:|
| `/eval/?url=[www.xyz.com]` | A ticket-number. Your evaluation request is queued and asynchronously started on the server as soon as Play! is ready to process it.
| `/ticket/[ticket-number]` | Either a ticket-number and the status for this ticket or the evaluation result if it's already available.*
| `/history/?url=[www.xyz.com]` | All evaluation results for this URL with the according date when the result was created|
*For the first time after the evaluation result is available, you get some additional information with the results. After that you receive a distilled version without any text.

**Example**

Let's say you want to evaluate the website **www.dary.info** by using Arignee's API whereas Arignee is running on your local machine on Port 9000 (default for Play applications).

- Triggering the evaluation with


    localhost:9000/eval/?url=www.dary.info

The response will be something like:

    {TICKET:"7dc53df5-703e-49b3-8670-b1c468f47f1f", STATUS: "QUEUING"}

- Requesting the Ticket-Status


    localhost:9000/ticket/7dc53df5-703e-49b3-8670-b1c468f47f1f

The response will be something like:

    {TICKET:"7dc53df5-703e-49b3-8670-b1c468f47f1f", STATUS: "EVAL_LINK_AMOUNT"}

This means that the request is being processed and the evaluation is happening at the moment. Some time later by again requesting `/ticket/[ticket-number]` the full evaluation is being returned as a json object which has a form similar  to:

    - RATING : GOOD
    - LINK
    -- RATING : EXCELLENT
    -- AMOUNT
    --- RATING : GOOD
    --- IDEAL : 123
    --- ACTUAL : 132
    -- EXTERNAL_BAKLINKS
    --- RATING : EXCELLENT
    .... etc. ....




Install
=======

**Build from Source**

If you want to build Arignee from the source on your own machine you need the Play! Framework installed (see Technologies). You should be able to deploy it by simply running the activator script in the root folder.

    ./activator run

However, you need an application.conf file in the /conf folder. (Tip: Try renaming the travis.conf file to application.conf, that might work).

Also you need a Database (I use PostgreSQL).

Last but not least, you might have to download some libraries (see Libraries) that are not automatically downloaded or integrated.

I will give some more detailed instructions on building the project once I'be got a working release.


**Running the Compiled Application**

I will upload the compiled application and give installation instructions once I've got a working release.

Technologies
============

- [Play-Framework 2.3.0](https://www.playframework.com/documentation/2.3.0/Home) - A Java Web-Framework

- [PostgreSQL](http://www.postgresql.org/) - Relational Database

Libraries
=========

Arignee depends on some vendor Libraries that are not included in the GIT repository.

**The JS libraries that are not included via CDN need to be downloaded and copied in the according directory**

**Java:**

Those libraries are automatically downloaded from a Ivy repository, since they
are listed in the build.sbt file.

- [Jsoup](http://jsoup.org/) v1.8.1 - Java HTML Parser

- [JSON in Java](http://www.json.org/java/)

- [Apache Commons Lang](http://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html)

- [Play-JSMessages](https://github.com/julienrf/play-jsmessages) v1.6.2 - Send localized Strings (i18n) to client

- [PostgreSQL JDBC Driver](http://jdbc.postgresql.org/) v9.1

**Javacript/CSS:**

- [Chart.js](https://github.com/nnnick/Chart.js) v1.0.1 - **!! Download** chart.js.min **and copy to** /public/javascripts/vendors

- [jQuery](http://jquery.com/) v2.1.1 - Added via CDN

- [Bootstrap](http://getbootstrap.com/) v3.3.1 - Added via CDN

License
=======

[GNU General Public License Version 2](http://www.gnu.org/licenses/gpl-2.0.html) (see LICENSE file)

Here is a quick&easy [Summary](https://tldrlegal.com/license/gnu-general-public-license-v2) of the License

Development
===========

v0.1-DEV - **unpublished**
