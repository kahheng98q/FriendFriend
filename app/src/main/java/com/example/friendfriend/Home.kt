package com.example.friendfriend


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.friendfriend.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {
    lateinit var selfName: String
    lateinit var bundle:Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

//        savedInstanceState!!.getString("key")
//        bundle=this.arguments!!
//
//         arguments?.get("key")?.let {
//             selfName = it.toString()
//             Toast.makeText(context, selfName,Toast.LENGTH_SHORT).show()
//        }
//        Toast.makeText(context, selfName,Toast.LENGTH_SHORT).show()
//        view!!.findNavController().navigate(R.id.confirmationAction, bundle)

        binding.messagecard.setOnClickListener{
//            bundle.putString("gg","ggg")
            view!!.findNavController().navigate(R.id.action_home_to_chatRoom,bundle)
        }
        binding.friendcard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home2_to_addedFriend2)
        }
        binding.schedulecard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home_to_confession_Page2)
        }
        binding.confessionPagecard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home_to_confession_Page2)
        }
        (activity as AppCompatActivity).supportActionBar?.title = "Home"
        setHasOptionsMenu(true)


        return binding.root

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        )
                || super.onOptionsItemSelected(item)
    }



}
