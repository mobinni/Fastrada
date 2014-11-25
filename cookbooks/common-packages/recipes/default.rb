#
# Cookbook Name:: nginx
# Recipe:: default
#
# Copyright 2013, Feedient
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#
common_packages = ['default-jre-headless', 'g++', 'git-core', 'curl', 'vim', 'nano', 'make']

common_packages.each do |pkg|
	package pkg do
		:upgrade
	end
end

directory "/home/vagrant/.m2" do
  owner "vagrant"
  group "vagrant"
  mode 00775
  action :create
end

template "/home/vagrant/.m2/settings.xml" do
  source "maven_settings.erb"
  mode 0755
  owner "vagrant"
  group "vagrant"
end
