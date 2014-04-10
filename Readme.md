POC of finagle RCP at Heroku.

To run the app at heroku, be sure to set up custom buildback:
heroku config:add BUILDPACK_URL=https://github.com/surg/heroku-buildpack-java.git

* master branch contains working version. It uses Netty HTTP tunnelling feature
* plain branch attempts to work directly via TCP connection which doesn't work