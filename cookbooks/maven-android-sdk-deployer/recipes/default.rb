execute "Install ANDROID_HOME" do
  command	'export ANDROID_HOME="/usr/local/android-sdk/"'
end

execute "git clone https://github.com/mosabua/maven-android-sdk-deployer.git" do
	cwd "/home/vagrant"
	user "root"
	not_if { File.exist?("/home/vagrant/maven-android-sdk-deployer") }
end

execute 'Install' do
  cwd			"/home/vagrant"
  command       "cd maven-android-sdk-deployer/ && mvn -pl com.simpligility.android.sdk-deployer:android-19 -am install && cd extras/compatibility-v4/ && mvn clean install"
  user          "root"
  group         "root"
end