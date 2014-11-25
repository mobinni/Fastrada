# Website Dedicated (Ubuntu Precise 12.04 LTS x86)
# x86 Since VirtualBox does not support x64 with hardware acceleration)
# To get a x64, run a production server or enable VT-x for the CPU (BIOS Settings)
# Configure the nodes, default ram = 256
nodes = [
    { :config=> 'teamb.localhost.com', :hostname => 'teamb.localhost.com', :ip => '192.168.0.6', :box => 'saucy-server-cloudimg-vagrant-i386-teamb', :url => 'http://cloud-images.ubuntu.com/vagrant/saucy/current/saucy-server-cloudimg-i386-vagrant-disk1.box', :ram => 512 },
]

Vagrant.configure("2") do |config|
    nodes.each do |node|
        config.vm.define node[:hostname] do |node_config|
            # User setup since we use vagrant in production to?
            #node_config.ssh.username = "feedient"

            nfs_setting = RUBY_PLATFORM =~ /darwin/ || RUBY_PLATFORM =~ /linux/

            # IF NO NFS: node_config.vm.synced_folder "projects", "/home/vagrant/projects", :owner => "www-data", :group => "www-data"
            node_config.vm.synced_folder "projects", "/home/vagrant/projects", :nfs => true


            node_config.vm.box = node[:box]
            node_config.vm.box_url = node[:url]
            node_config.vm.hostname = node[:hostname]

            config.ssh.forward_agent = true
            node_config.vm.network :private_network, ip: node[:ip]

            # Virtualbox provider
            memory = node[:ram] ? node[:ram] : 256;
            node_config.vm.provider :virtualbox do |vb|
                vb.customize [
                    'modifyvm', :id, 
                    '--name', node[:hostname],
                    '--memory', memory.to_s
                ]
            end

            # Shell provisioner, we need puppet!
            node_config.vm.provision :shell, :inline => "sh /vagrant/install.sh #{node[:config]} /vagrant/"
        end
    end 
end
