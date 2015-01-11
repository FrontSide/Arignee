[![Stories in Ready](https://badge.waffle.io/FrontSide/Arignee.png?label=ready&title=Ready)](https://waffle.io/FrontSide/Arignee) [![Build Status](https://travis-ci.org/FrontSide/Arignee.svg?branch=master)](https://travis-ci.org/FrontSide/Arignee)

Arignée
=======

A Web-Application for automated evaluations of e-commerce websites.

Background
==========

This Application is Part of my Bachelor Thesis I for the Bachelor of Science in Computing Degree Programme.

Technologies
============

- [Play-Framework 2.3.0](https://www.playframework.com/documentation/2.3.0/Home)
  A Java Web-Framework

- [PostgreSQL](http://www.postgresql.org/)
  Relational Database

Libraries
=========

Arignee depends on some vendor Libraries that are not included in the GIT repository.

**The JS libraries that are not included via CDN need to be downloaded and copied in the according directory**

**Java:**

Those libraries are automatically downloaded from a Ivy repository, since they
are listed in the build.sbt file.

- [Jsoup](http://jsoup.org/) v1.8.1

  Java HTML Parser

- [JSON in Java](http://www.json.org/java/)

- [Play-JSMessages](https://github.com/julienrf/play-jsmessages) v1.6.2

  Send localized Strings (i18n) to client

- [PostgreSQL JDBC Driver](http://jdbc.postgresql.org/) v9.1

**Javacript/CSS:**

- [Chart.js](https://github.com/nnnick/Chart.js) v1.0.1

  **!! Download** chart.js.min **and copy to** /public/javascripts/vendors

- [jQuery](http://jquery.com/) v2.1.1

  Added via CDN

- [Bootstrap](http://getbootstrap.com/) v3.3.1

  Added via CDN

License
=======

[GNU General Public License Version 2](http://www.gnu.org/licenses/gpl-2.0.html) (see LICENSE file)

Here is a quick&easy [Summary](https://tldrlegal.com/license/gnu-general-public-license-v2) of the License

Development
===========

v0.1-DEV - **unpublished**
